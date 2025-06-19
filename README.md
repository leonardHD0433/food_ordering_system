# Food Ordering System

## Prerequisite

### Database Setup

This project requires a MySQL database. You can set it up 

1. Using **Docker** (recommended) : So that you don't have to install an entire server on your machine
2. Install MySQL directly on your system.

#### Option 1: Using Docker (Docker Compose) (Recommended)

1. Make sure you have [Docker](https://docs.docker.com/get-docker/) available on your machine.
2. Make sure you are in your workspace path (.../food_ordering_system) 
3. Duplicate and rename the `.env.template` to `.env` and fill in the environment variables as shown below
    ```
    DB_HOST= localhost      //required
    DB_PORT= 3307           //required
    DB_NAME= fos            //required
    DB_USER= admin          //your own
    DB_PASSWORD= admin123   //your own
    ```
    - The database container will be accessible at `localhost:3307` using non-standard port to prevent potential clashing with existing mysql server if installed.
4. Run the following command to start a MySQL container:
   ```bash
   docker-compose up -d
   ```
5. (Optional) To stop and remove the container:
   ```bash
   docker-compose down
   ```

#### Option 2: Install MySQL Directly

- **Linux:** [How to Install MySQL on Linux](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-20-04)
- **Windows:** [How to Install MySQL on Windows](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/windows-installation.html)

Duplicate and rename the `.env.template` to `env` and fill in the environment variables as shown below
```
DB_HOST= localhost      //required
DB_PORT= 3306           //required
DB_NAME= fos            //required
DB_USER= admin          //your own
DB_PASSWORD= admin123   //your own
```
Difference: If mysql is installed directly installed on your system, the database is most likely be accessible at its default port `localhost:3306`.

## Running

1. Clone this repository:
   ```bash
   git clone https://github.com/leonardHD0433/food_ordering_system.git
   cd food_ordering_system
   ```
2. Make sure you have Java **(Make sure it's JDK 21)** and Maven installed.

    If not:

    - [Download Java (JDK 21)](https://adoptium.net/temurin/releases/?version=21)
    - [Java Documentation](https://docs.oracle.com/en/java/javase/21/)
    - [Maven Download & Installation Guide](https://maven.apache.org/install.html)
    - [Maven Documentation](https://maven.apache.org/guides/index.html)

3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application (Make sure the database `server` or `container` is up & running first):
   ```bash
   mvn clean javafx:run
   ```

---

For more details, refer to the project documentation or contact the maintainer.
