from django.urls import path, include

from .views import (api_account_properties_view, api_register_user_view, activate,
                    api_edit_account_view, ObtainAuthTokenView, ChangePasswordView, api_reset_password_view)
from django.conf.urls import url


app_name = 'account'

urlpatterns = [
    path('change_password', ChangePasswordView.as_view(), name='change_password'),
    path('login', ObtainAuthTokenView.as_view(), name='login'),
    path('register', api_register_user_view, name='register'),
    path('reset_password', api_reset_password_view, name='reset_password'),
    path('edit', api_edit_account_view, name='edit_account'),
    path('<username>/properties', api_account_properties_view, name='account_properties'),
    url(r'^activate/(?P<uidb64>[0-9A-Za-z_\-]+)/(?P<token>[0-9A-Za-z]{1,13}-[0-9A-Za-z]{1,20})/$',
        activate, name='activate'),
    
]

