from django.db import models
from django.utils import timezone

from django.contrib.auth.base_user import AbstractBaseUser, BaseUserManager

from rest_framework.authtoken.models import Token
from django.db.models.signals import post_save
from django.conf import settings
from django.dispatch import receiver
from django.core.validators import RegexValidator


class UserManager(BaseUserManager):
    def create_user(self, username, first_name, last_name, email, password=None, image=None,):
        if not username:
            raise ValueError('Users must have a username')
        if not first_name:
            raise ValueError('Users must have a first name')
        if not last_name:
            raise ValueError('Users must have a last name')
        if not email:
            raise ValueError('Users must have an email address')

        user = User(
            username=username,
            first_name=first_name,
            last_name=last_name,
            email=self.normalize_email(email),
            image=image
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, username, first_name, last_name, email, password):
        user = self.create_user(
            username=username,
            first_name=first_name,
            last_name=last_name,
            email=self.normalize_email(email),
            password=password
        )

        user.is_admin = True
        user.is_staff = True
        user.is_superuser = True

        user.save(using=self._db)
        return user 


class User(AbstractBaseUser):
    first_name =        models.CharField(max_length=30)
    last_name =         models.CharField(max_length=40)
    username =          models.CharField(max_length=30, unique=True, null=False, blank=False, 
                            validators=[RegexValidator('^(?=.{8,30}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$', message="Username can only contain alphabets, numbers, '_', or '.' in an accepted form;\nAnd, it should be 8 to 30 characters long.")])
    email =             models.EmailField(max_length=100, unique=True)

    image =             models.ImageField(upload_to='images/', blank=True)
    books_count =       models.PositiveIntegerField(default=0, blank=True)
    rating =            models.FloatField(default=0.0, blank=True)

    date_joined =       models.DateTimeField(verbose_name='date joined', auto_now_add=True)
    last_login =        models.DateTimeField(verbose_name='last login', auto_now=True)

    is_admin =          models.BooleanField(default=False)
    is_active =         models.BooleanField(default=True)
    is_staff =          models.BooleanField(default=False)
    is_superuser =      models.BooleanField(default=False)

    REQUIRED_FIELDS =   ['first_name', 'last_name', 'email']
    USERNAME_FIELD =    'username'
    EMAIL_FIELD =       'email'
    
    objects = UserManager()

    def has_perm(self, perm, obj=None):
        #"Does the user have a specific permission?"
        # Simplest possible answer: Yes, always
        return self.is_admin

    def has_module_perms(self, app_label):
        #"Does the user have permissions to view the app `account`?"
        # Simplest possible answer: Yes, always
        return True

    @property
    def full_name(self):
        return self.first_name + ' ' + self.last_name

    def get_full_name(self):
        return self.first_name + ' ' + self.last_name
    def get_short_name(self):
        return self.first_name

    def __str__(self):
        return self.username

    class META:
        fields = ('first_name', 'last_name', 'email', 'image',)


@receiver(post_save, sender=settings.AUTH_USER_MODEL)
def create_auth_token(sender, instance=False, created=False, **kwargs):
    if created:
        Token.objects.create(user=instance)