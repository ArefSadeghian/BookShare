from django.urls import path, include

from .views import (api_get_user_info_view, api_register_user_view,
                    api_edit_user_info_view, ObtainAuthTokenView)


app_name = 'account'

urlpatterns = [
    path('register', api_register_user_view, name='register'),
    path('login', ObtainAuthTokenView.as_view(), name='login'),
    path('<username>', api_get_user_info_view, name='get_user_info'),
    path('<username>/edit', api_edit_user_info_view, name='edit_user_info'),
]

