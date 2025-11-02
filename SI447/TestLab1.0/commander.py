import requests


class Commander:
    """
    Interface to the Mids Quest API.
    This class is responsible for translating commands in our
    system to AP calls.
    """
    def create_user(self, username, password):
        """
        This function asks the API to create a new user. If the response
        is 200, the user was created successfully. If 400, the user was not
        created successfuclly.

        This function will return void. It will raise an exception if 
        unsuccessful.
        """
        url ="http://lnx1073302govt:8000/user/"
        
        body = {
        "username": username,
        "password": password
        }
        
        response = requests.post(url, json=body)

        if response.status_code == 200:
            return
        
        raise Exception

    
    def login(self):
        pass
    
    def move(direction):
        """
        Direction is a string, either 'N', 'S', 'E', or 'W'
        """
        pass
    
    def look():
        """
        returns a string which is a description of the current
        room. (Just returns the string that the server returns).
        """
        pass
    
