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
    password_repeat = serializers.CharField(style={'input_type': 'password'}, write_only=True)

    class Meta:
        model = User
        fields = ['first_name', 'last_name', 'username', 'password', 'password_repeat']
        extra_kwargs = {
            'password_repeat': {'write_only': True}
        }

    def save(self):
        password = self.validated_data['password']
        password_repeat = self.validated_data['password_repeat']
        
        if password != password_repeat:
            raise serializers.ValidationError({'password': 'Passwords must match!'})

        edited_user = self.instance

        edited_user.username = self.validated_data['username']
        edited_user.first_name = self.validated_data['first_name']
        edited_user.last_name = self.validated_data['last_name']
        edited_user.set_password(password)
        
        edited_user.save()
        
        return edited_user


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

        new_user = User.objects.create_user(
            username = self.validated_data['username'],
            first_name = self.validated_data['first_name'],
            last_name = self.validated_data['last_name'],
            password = password,
            email = self.validated_data['email'],
            image = self.validated_data['image']
        )

        return new_user

