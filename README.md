# <span style="color:cornflowerblue">University Sports Matching App</span>

The University Sports Matching App is a web-based application that helps university students find and connect with other players based on their preferences for sport, campus, rating, and time availability. The application is built with Java, Spring, JPA, Spring Security, MySQL, Docker, Kubernetes, GCP, and JWT, and follows a restful architecture.

## <span style="color:cornflowerblue">Features</span>

- User authentication and authorization
- User registration and profile management
- Campus and sport selection
- Availability management
- Team and game creation
- Team and game search based on user preferences

## <span style="color:cornflowerblue">Getting Started</span>

To get started with the University Sports Matching App, you'll need to have the following tools and technologies installed:

- Java 17
- Docker
- Kubernetes
- MySQL

<span style="color:cornflowerblue">To set up the application, follow these steps:</span>

1. Clone the repository to your local machine`git clone https://github.com/ReinoldoCaceres/sports-app.git
   `
2. Navigate to the project directory:`cd sports-app`
3. Configure the database connection:

   •Open the `src/main/resources/application.yml` file.

   •Update the following properties with your MySQL database configuration:
    ```
   spring:
      datasource:
          url: jdbc:mysql://localhost:3306/sportsapp
          username: your-username
          password: your-password
      ```
4. Build the project using Maven: `mvn clean package`
5. Run the application: `java -jar target/sportsapp.jar`
5. The application should now be accessible at http://localhost:8080.

## <span style="color:cornflowerblue">Usage</span>

### <span style="color:cornflowerblue">Authentication</span>

To use the University Sports Matching App, you'll need to register and create an account. Once you've created an account, you'll be able to log in and access the app's features.

### <span style="color:cornflowerblue">Profile Management</span>

After logging in, you'll be able to manage your profile by updating your personal information, campus, and sport preferences.

### <span style="color:cornflowerblue">Availability Management</span>

You'll also be able to manage your availability by indicating your preferred days of the week and times for playing.

### <span style="color:cornflowerblue">Team and Game Creation</span>

If you're interested in creating a team or a game, you can do so by filling out the required information on the app's Create Team or Create Game pages. You'll be able to specify the sport, campus, start time, and end time, as well as any other details you'd like to include.

### <span style="color:cornflowerblue">Team and Game Search</span>

Finally, you can search for teams or games based on your preferences by using the app's Search Teams or Search Games pages. You can filter your search results by sport, campus, rating, and time availability.

## <span style="color:cornflowerblue">Contributing</span>

If you're interested in contributing to the University Sports Matching App, please feel free to email me. We welcome all contributions and feedback! :smile:
