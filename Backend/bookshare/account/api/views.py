from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from django.contrib.auth import authenticate
from rest_framework.generics import UpdateAPIView
from account.tokens import account_activation_token

from rest_framework.authtoken.models import Token
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView

from account.models import User
from .serializers import (UserSerializer, SelfUserSerializer, EditedUserSerializer, 
                            UserRegisterationSerializer, ChangePasswordSerializer)
from django.contrib.sites.shortcuts import get_current_site
from django.template.loader import render_to_string
from django.utils.http import urlsafe_base64_decode, urlsafe_base64_encode
from django.utils.encoding import force_bytes, force_text
from django.core.mail import EmailMessage, send_mail


@api_view(['POST', ])
@permission_classes([])
@authentication_classes([])
def api_register_user_view(request):
    
    if request.method == 'POST':        
        data = {}

        email = request.data.get('email', '0_no_email_provided_0').lower()
        username = request.data.get('username', '0_no_username_provided_0')

        if email == '0_no_email_provided_0':
            data['response'] = 'Error'
            data['erroe_message'] = 'No email was provided!'
            return Response(data)
        if username == '0_no_username_provided_0':
            data['response'] = 'Error'
            data['erroe_message'] = 'No username was provided!'
            return Response(data)
        
        if validate_email(email) is not None:
            data['response'] = 'Error'
            data['error_message'] = 'Sorry, user with this email already exists!'
            return Response(data)
        if validate_username(username) is not None:
            data['response'] = 'Error'
            data['erroe_message'] = 'Sorry, user with this username already exists!'
            return Response(data)

        # lowercase email:
        request_data = request.data.copy()
        if 'email' in request_data:
            request_data['email'] = email
        serializer = UserRegisterationSerializer(data=request_data)

        if serializer.is_valid():
            new_user = serializer.save()

            # email verification:
            new_user.is_active = False
            new_user.save()
            current_site = get_current_site(request)
            mail_subject = 'Activate your account'
            activation_token = account_activation_token.make_token(new_user)
            mail_message = render_to_string('activate_email_email.html', {
                        'user': new_user,
                        'domain': current_site.domain,
                        'uid': urlsafe_base64_encode(force_bytes(new_user.pk)),
                        'token': activation_token,
                    })
           
            email_destination = email
            EmailMessage(mail_subject, mail_message, to=[email_destination]).send()
            return Response({'response': 'Please confirm your email address to complete the registration.'}, status=status.HTTP_200_OK)
        
        else:
            data = serializer.errors
            return Response(data)


@api_view(['GET', ])
@permission_classes([])
@authentication_classes([])
def activate(request, uidb64, token):
    try:
        uid = force_text(urlsafe_base64_decode(uidb64))
        user = User.objects.get(pk=uid)
    except(TypeError, ValueError, OverflowError, User.DoesNotExist):
        user = None
    
    if user is not None and account_activation_token.check_token(user, token):
        user.is_active = True
        user.save()

        data = {}
        data['response'] = 'Successfully registered'
        token = Token.objects.get(user=user).key
        data['token'] = token
        data['username'] = user.username
        data['email'] = user.email
        data['first_name'] = user.first_name
        data['last_name'] = user.last_name
        return Response(data, status=status.HTTP_200_OK)

    else:
        return Response({'response': 'Error', 'error_message': 'Activation link is invalid!'})


@api_view(['POST', ])
@permission_classes((IsAuthenticated,))
def api_reset_password_view(request):
    if request.method == 'POST':
        email = request.data.get('email', '0_no_email_provided_0').lower()

        data = {}
        if email == '0_no_email_provided_0':
            data['response'] = 'Error'
            data['erroe_message'] = 'No email was provided!'
            return Response(data)

        if validate_email(email) is None:
            data['response'] = 'Error'
            data['error_message'] = 'Sorry, user with this email deos not exists!'
            return Response(data)

        user = User.objects.get(email=email)
        new_password = User.objects.make_random_password()
        user.set_password(new_password)
        user.save()

        # email verification:
        mail_subject = 'Retrieve your account password'
        mail_message = render_to_string('reset_password_email.html', {
                    'user': user,
                    'new_password': new_password,
                })
        
        email_destination = email
        EmailMessage(mail_subject, mail_message, to=[email_destination]).send()
        return Response({'response': 'We sent a new password to your email account, so Please chack that.'}, status=status.HTTP_200_OK)


def validate_email(email):
    user_with_this_email = None
    try:
        user_with_this_email = User.objects.get(email=email)
    except User.DoesNotExist:
        return None
    if user_with_this_email is not None:
        return email
    return None


def validate_username(username):
    user_with_this_username = None
    try:
        user_with_this_username = User.objects.get(username=username)
    except User.DoesNotExist:
        return None
    if user_with_this_username:
        return username
    return None


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
            data['username'] = user.username
            data['email'] = user.email
            data['first_name'] = user.first_name
            data['last_name'] = user.last_name
            return Response(data, status=status.HTTP_200_OK)
        else:
            data['response'] = 'Error'
            data['error_message'] = 'Invalid credentials'
            return Response(data)


@api_view(['GET', ])
@permission_classes((IsAuthenticated,))
def api_account_properties_view(request, username):
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
def api_edit_account_view(request):
    try:
        user = request.user
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'PUT':
        if not any([field in request.data for field in ['first_name', 'last_name', 'username']]):
            return Response(data={'response': 'No changes made'}, status=status.HTTP_304_NOT_MODIFIED)
        serializer = EditedUserSerializer(user, data=request.data, partial=True)
        data = {}
        
        if serializer.is_valid():
            serializer.save()
            return Response(data={'response': 'Successfully updated account'}, status=status.HTTP_200_OK)
            
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class ChangePasswordView(UpdateAPIView):
	serializer_class = ChangePasswordSerializer
	model = User
	permission_classes = (IsAuthenticated,)
	authentication_classes = (TokenAuthentication,)

	def get_object(self, queryset=None):
		obj = self.request.user
		return obj

	def update(self, request, *args, **kwargs):
		self.object = self.get_object()
		serializer = self.get_serializer(data=request.data)

		if serializer.is_valid():
			# Check old password
			if not self.object.check_password(serializer.data.get("old_password")):
				return Response({"response": "Error", "error_message": "The old password is wrong."}, status=status.HTTP_400_BAD_REQUEST)

			# confirm the new passwords match
			new_password = serializer.data.get("new_password")
			confirm_new_password = serializer.data.get("new_password_confirmation")
			if new_password != confirm_new_password:
				return Response({"response": "Error", "error_message": "New passwords must match."}, status=status.HTTP_400_BAD_REQUEST)

			# set_password also hashes the password that the user will get
			self.object.set_password(serializer.data.get("new_password"))
			self.object.save()
			return Response({"response": "Successfully changed password"}, status=status.HTTP_200_OK)

		return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
