# Space Book
Space book is a socialnetwork application implemented using JavaFx.

# Features
# Chat Groups
The user can see all groups he's part of by clicking on the "Messages" icon on top right. The main view will display a list of all conversations and the messages of
the selected conversation. New groups can be created by clicking on the "New Conversation" button on top of the list of conversations. A new window will open in which
the user can select the participants of the group by searching by name and then entering the name and the description.
![Screenshot 2022-03-30 175551](https://user-images.githubusercontent.com/93886764/160865136-3cd7c446-b934-4d4c-a75a-dc6aa3f77501.png)

![Screenshot 2022-03-30 175712](https://user-images.githubusercontent.com/93886764/160865542-8ffc4ad8-2bd4-44f7-8c5c-b6b2072489d6.png)

# Events
The user can create and sign up to an event. After signing up, if the event date is close to the current date, the user will be notified. The event notification system
uses a separate thread that from time to time checks if the logged user has signed up to an event, so that he will be notified during runtime and not only after he
logs. Also, the user can disable notifications for upcomming events.

![Screenshot 2022-03-30 180321](https://user-images.githubusercontent.com/93886764/160867074-4b81706b-d41d-4e29-93d7-f0c742eb4200.png)

![Screenshot 2022-03-30 180408](https://user-images.githubusercontent.com/93886764/160867266-011a919b-6b18-4efa-bbce-709097e2b521.png)

# Notifications and Friend requests
Search after users first name and last name can be done using the top text field and then clicking on the "Search" icon. A pagination with links to the found users 
profiles will be displayed. After selecting one of the links, the main page of the user will be displayed, with information regards the status of the friend request,
friends of user and contact info. If the user that received a friend request declines it, the sender can't send again a new request.

![Screenshot 2022-03-30 180916](https://user-images.githubusercontent.com/93886764/160868624-d01f2745-9136-4565-84b8-7490a68f5617.png)

Request was sent
![Screenshot 2022-03-30 180958](https://user-images.githubusercontent.com/93886764/160868767-bf4d7d2b-cdcd-4470-b8aa-c92398e52217.png)

Received friend requests can be displayed by clicking on the "Notifications" icon. A list with upcoming events, pending, accepted or declined friend requests will be 
displayed in the main view. A request can be accepted or declined by clicking on the buttons "Accept"/"Decline".

Viewing notifications
![Screenshot 2022-03-30 181331](https://user-images.githubusercontent.com/93886764/160869597-50e9b94b-e7ef-4957-8919-048e4c086b3e.png)

# Reports
Received messages and new friends from a month of year can be displayed by selecting "Activity report" button on the left menu, then entering the month and year and
finally clicking on "Preview reports".

Viewing new friends and received messages
![image](https://user-images.githubusercontent.com/93886764/160870207-daeab165-f346-4517-8baa-ec7ac6a405d0.png)

Also, messages received from a certain user can be displayed by selecting "Messages report", searching for the sender by using the bottom search text field and search
button, selecting the sender from the search result from the pagination on the top right and finally clicking on "Preview report".

Viewing messages received from a user 
![Screenshot 2022-03-30 181945](https://user-images.githubusercontent.com/93886764/160870856-9a4a099a-6c07-4b20-b85c-e80089e398b3.png)

The application can save these reports in a pdf file by clicking on the "Save as" button. A "Save as" dialog window will be opened where the user can select the 
location of the file. For the implementation we used apache pdfbox library. Bellow you can see an example for each type of report.

Activity report
[report.pdf](https://github.com/paul-maga-pm/space_book/files/8381803/report.pdf)

Messages report
[messages_report.pdf](https://github.com/paul-maga-pm/space_book/files/8381827/messages_report.pdf)

# Implementation Details
The application is based on a layered architecture as follows: Domain (models and validators), Repository (for persistence), Services (which use the persistence layer 
and implement the business rules of the application), JavaFx Controlllers and Views (MVC pattern).

The data is persisted on a local Postgresql database. The communication with the database is managed only through the repository which implement a common interface,
so the business rules will not change if the persistence details change.

The owner of the database is postgres. Connection details regarding url, user and password can be configured in the config.properties resource file.

Bellow is the script for generating the database:
[generate_database.zip](https://github.com/paul-maga-pm/space_book/files/8381964/generate_database.zip)

# Prerequisites
Gradle build system for Java
Postgresql server for local database