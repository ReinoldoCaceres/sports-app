# **University Sports Matching App**

The University Sports Matching App is a web-based application that helps university students find and connect with other players based on their preferences for sport, campus, rating, and time availability. The application is built with Java, Spring, JPA, Spring Security, MySQL, Docker, Kubernetes, GCP, and JWT, and follows a restful architecture.
Features

## **The University Sports Matching App has the following features:**

    User authentication and authorization
    User registration and profile management
    Campus and sport selection
    Availability management
    Team and game creation
    Team and game search based on user preferences

## **Getting Started**

To get started with the University Sports Matching App, you'll need to have the following tools and technologies installed:

    Java 8+
    Docker
    Kubernetes
    MySQL

## **To set up the application, follow these steps:**

  1.Clone the repository to your local machine

  2.Build the Docker image by running the following command in the project root directory:
  
  `docker build -t university-sports-matching-app .`
  
  3.Start the MySQL database by running the following command:
  
  `kubectl apply -f kubernetes/mysql-deployment.yaml`

  4.Start the application by running the following command:
  
  `kubectl apply -f kubernetes/app-deployment.yaml`
  
  5.The application should now be accessible at http://localhost:8080.
  
## **Usage**

**Authentication**

To use the University Sports Matching App, you'll need to register and create an account. Once you've created an account, you'll be able to log in and access the app's features.

**Profile Management**

After logging in, you'll be able to manage your profile by updating your personal information, campus, and sport preferences.
**
Availability Management**

You'll also be able to manage your availability by indicating your preferred days of the week and times for playing.

**Team and Game Creation**

If you're interested in creating a team or a game, you can do so by filling out the required information on the app's Create Team or Create Game pages. You'll be able to specify the sport, campus, start time, and end time, as well as any other details you'd like to include.
**
Team and Game Search**

Finally, you can search for teams or games based on your preferences by using the app's Search Teams or Search Games pages. You can filter your search results by sport, campus, rating, and time availability.

## **Contributing**

If you're interested in contributing to the University Sports Matching App, please feel free to email me. We welcome all contributions and feedback!
