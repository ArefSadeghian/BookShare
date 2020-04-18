from rest_framework import serializers

from account.models import User


class SelfUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'

    
class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['first_name', 'last_name', 'username', 'email', 'image', 'books_count', 'rating']


class EditedUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['first_name', 'last_name', 'username']


class UserRegisterationSerializer(serializers.ModelSerializer):
    password_repeat = serializers.CharField(style={'input_type': 'password'}, write_only=True)

    class Meta:
        model = User
        fields = ['first_name', 'last_name', 'username', 'email', 'image', 'password', 'password_repeat']
        extra_kwargs = {
            'password_repeat': {'write_only': True}
        }

    def save(self):
        password = self.validated_data['password']
        password_repeat = self.validated_data['password_repeat']
        
        if password != password_repeat:
            raise serializers.ValidationError({'password': 'Passwords must match!'})

        image = ''
        if 'image' in self.validated_data:
            image = self.validated_data['image']
        new_user = User.objects.create_user(
            username = self.validated_data['username'],
            first_name = self.validated_data['first_name'],
            last_name = self.validated_data['last_name'],
            password = password,
            email = self.validated_data['email'],
            image = image
        )

        return new_user


class ChangePasswordSerializer(serializers.Serializer):
    old_password = serializers.CharField(required=True)
    new_password = serializers.CharField(required=True)
    new_password_confirmation = serializers.CharField(required=True)
    