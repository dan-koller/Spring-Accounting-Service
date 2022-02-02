# Spring accounting service
This is a rather large project which is based on an idea from JetBrains Academy.
At its core, it is a REST-API service which enables tracking salary data, view received
salaries for selected employees, get a list of employees on a payroll (all or for a certain period).

## Endpoints
There are several endpoints avaibale based on different usecases.

### \> Signing up
**POST**  `/api/auth/signup`: Sign up new users for the service. Request body:

        {
            "name": "John",
            "lastname": "Doe",
            "email": "johndoe@yourmail.com",
            "password": "secretPassw0rd"
        }

_Be advised that the first user to sign up will become the administrator. All others will become user by default.
You can change the roles later on as admin._

**POST**  `/api/auth/changepass`: Allow users with valid authentication (_e.g. as johndoe@yourmail.com_) to change their password.
Request body:

        {
            "new_password": "sUpErSeCrEt123!"
        }

_The password gets verified, that it isn't in a stolen password database (see src/misc/PasswordBlacklist.java) and that it
also matches security regulations (min. length of 12 characters)._

### \> Accounting
**POST**  `api/acct/payments`: Upload payment data following the JSON form:

    [
        {
            "employee": "<user email>",
            "period": "<mm-YYYY>",
            "salary": <Long>
        },
        {
            "employee": "<user1 email>",
            "period": "<mm-YYYY>",
            "salary": <Long>
        },
    ]

**PUT**  `api/acct/payments`: Update salary data for specified user:

        {
            "employee": "<user email>",
            "period": "<mm-YYYY>",
            "salary": <Long>
        }

**GET**  `/api/empl/payment`: Returns specified payment data (e.g. _api/empl/payment?period=01-2021_):

        {
           "name": "John",
           "lastname": "Doe",
           "period": "March-2021",
           "salary": "1234 dollar(s) 56 cent(s)"
        }

or an entire list of data for the requesting user:

    [
        {
           "name": "John",
           "lastname": "Doe",
           "period": "March-2021",
           "salary": "1234 dollar(s) 56 cent(s)"
        },
        {
           "name": "John",
           "lastname": "Doe",
           "period": "February-2021",
           "salary": "1234 dollar(s) 56 cent(s)"
        },
        {
           "name": "John",
           "lastname": "Doe",
           "period": "January-2021",
           "salary": "1234 dollar(s) 56 cent(s)"
        }
    ]

### \> Administration
**GET**  `/api/admin/user`: Obtain all non-senstive data from registered users.

**DELETE**  `/api/admin/user/{email}`: Delete a user by specifying their email address in the url

    {
        "user": "<user email>",
        "status": "Deleted successfully!"
    }

**DELETE**  `/api/admin/user`: A "fake" that returns an error that you need to specify which user to delete.

**PUT**  `/api/admin/user/role`: Update roles for specified user(s):

    {
        "user": "administrator@yourmail.com",
        "role": "AUDITOR",
        "operation": "GRANT"
    }

**PUT**  `/api/admin/user/access`: This endpoint (un-)locks users:

    {
        "user": "<String value, not empty>",
        "operation": "<[LOCK, UNLOCK]>"
    }

### \> Auditing
**GET**  `"/api/security/events"`: Returns a list of all security relevant events (_e.g. deleted user from whom, added roles, bruteforce attacks, etc._)

### Restrictions
Based on their given role, user can access the above endpoints as follows:

|   | Public  | User  | Accountant  | Administrator  | Auditor |
|---|---|---|---|---|---|
| POST api/auth/signup  | +  | +  | +  | +  | +  |
| POST api/auth/changepass  |   | +  | +  | +  | -  |
| GET api/empl/payment  | -  | +  | +  | -  | -  |
| POST api/acct/payments  | -  | -  | +  | -  | -  |
| PUT api/acct/payments  | -  | -  | +  | -  | -  |
| GET api/admin/user  | -  | -  | -  | +  | -  |
| DELETE api/admin/user  | -  | -  | -  | +  | -  |
| PUT api/admin/user/role  | -  | -  | -  | +  | -  |
| PUT api/admin/user/access  | -  | -  | -  | +  | -  |
| GET api/security/events  | -  | -  | -  | -  | +  |

_Of course, you can change the accessabilty of certain endpoints based on your needs._

## Installation

- `./gradlew build --refresh-dependencies` to install dependencies
- The application is secured by https
- However, I used a selfsigned certificate which shouldn't be used in a productive environment. Get a certificate
from a trustworthy source like [Let's encrypt](https://letsencrypt.org/getting-started/).

## Technologies

- Java 17 LTS
- Spring (MVC, Web, Security)
- SQL [H2 Database for testing, MySQL for deployment (any other SQL database with ORM might work as well)]
- Gradle 7.3 (build)

## Roadmap
This software is in an early stage of development. Beside API functions I'm planning on adding features such as:  

- Web interface (Access program functionality from a clean UI)
