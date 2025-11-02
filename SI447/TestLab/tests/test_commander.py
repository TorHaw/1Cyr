import pytest
from commander import Commander

class TestCommander:

    def test_create_user(uname, pword):
        # step 1: set up
        commander = Commander()

        # Step 2: Run Code
        username = "test_user_34"
        password = "password34"
        
        # Step 3: Assert
        # If unsuccessful, creat_user should trhow an error
        commander.create_user(username, password)

        # Now ensure can't creat the same user twice
        with pytest.raises(Exception) as e:
            commander.create_user(username, password)

        


    def test_user_login(username, password):
        pass
    
    def test_move(self):
        pass
    
    def test_look(self):
        pass
    


