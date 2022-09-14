OSlash – Integration Engineering Assignment (Java)


    Use the Google API to connect, fetch all documents currently existing, and then listen for changes on a particular Google Drive folder using WebHook.
    Writes into a json file every 'n' batch size
    
Instructions to Run this project:

    Open this in an IDE preferably Intellij
    Add Spring Boot Run Configuration with com.gunish.oslashassignment.OslashAssignmentApplication as main Class and Run, the project will run on port 8080
    Another important thing required is to port forward 8080 port using ngrok as we are using web hook to receive notifications and for that we have to create a Channel
    whose address requires a https url.
    So update that url in GoogleDriveServiceImpl for now,
    channel.setAddress("https://1c09-103-21-185-12.in.ngrok.io/notifications");
    
    
