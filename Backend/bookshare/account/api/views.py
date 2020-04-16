from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes
from django.contrib.auth import authenticate

from rest_framework.authtoken.models import Token
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView

from account.models import User
from .serializers import (UserSerializer, SelfUserSerializer, EditedUserSerializer, 
                            UserRegisterationSerializer)


@api_view(['POST', ])
def api_register_user_view(request):
    if request.method == 'POST':
        serializer = UserRegisterationSerializer(data=request.data)
        data = {}
        if serializer.is_valid():
            new_user = serializer.save()
            data['success'] = 'ok'
            token = Token.objects.get(user=new_user).key
            data['token'] = token
        else:
            data = serializer.errors
        
        return Response(data)


class ObtainAuthTokenView(APIView):
    authentication_classes = []
    permission_classes = []
    
    def post(self, request):
        username = request.POST.get('username')
        password = request.POST.get('password')

        data = {}
        user = authenticate(username=username, password=password)

        if user:
            try:
                token = Token.objects.get(user=user)
            except Token.DoesNotExist:
                token = Token.objects.create(user=user)
            data['response'] = 'Successfully authenticated'
            data['token'] = token.key
        else:
            data['response'] = 'Error'
            data['errors'] = 'Invalid credentials'

        return Response(data)


@api_view(['GET', ])
@permission_classes((IsAuthenticated,))
def api_get_user_info_view(request, username):
    try:
        user = User.objects.get(username=username)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    requester = request.user
    is_outsider = (user != requester)

    if request.method == 'GET':
        serializer = UserSerializer(user)
        if not is_outsider:
            serializer = SelfUserSerializer(user)
        return Response(serializer.data)


@api_view(['PUT', ])
@permission_classes((IsAuthenticated,))
def api_edit_user_info_view(request, username):
    try:
        user = User.objects.get(username=username)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    requester = request.user
    is_outsider = (user != requester)
    if is_outsider:
        return Response({'success': 'not successful'})

    if request.method == 'PUT':
        serializer = EditedUserSerializer(user, data=request.data)
        data = {}
        
        if serializer.is_valid():
            serializer.save()
            data['success'] = 'ok'
            return Response(data=data)
            
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)



        