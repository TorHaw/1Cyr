import requests

url ="http://lnx1073302govt:8000/user/"

body = {
    "username": "test_user_1",
    "password": "test_password_1"
}

response = requests.post(url, json=body)

print(response.status_code)
print(response.text)

