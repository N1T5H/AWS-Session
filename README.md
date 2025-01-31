Deploying a Java Nonogram Game Application on AWS EC2

## Testing the project locally

1. Clone the project repository:
```bash
git clone https://github.com/your-username/java-nonogram-game.git
```

2. Navigate to the project directory:
```bash
cd java-nonogram-game
```

3. Compile and run the application:
```bash
javac NonogramGame.java
java NonogramGame
```

## Setting up an AWS EC2 instance

1. Create an IAM user with AdminAccess permissions and login to your AWS Console.

2. Launch an EC2 instance:
   - Choose an Amazon Linux 2 AMI
   - Select t2.micro instance type
   - Create a new key pair and download the .pem file
   - Configure security group to allow SSH (port 22) and custom TCP (port 8080) inbound traffic

3. Connect to the instance using SSH:
```bash
ssh -i your-key.pem ec2-user@<instance-public-ip>
```

## Configuring the EC2 instance

1. Update the system packages:
```bash
sudo yum update -y
```

2. Install Java Development Kit (JDK):
```bash
sudo amazon-linux-extras install java-openjdk11 -y
```

3. Verify Java installation:
```bash
java -version
```

## Deploying the Java Nonogram Game

1. Install Git:
```bash
sudo yum install git -y
```

2. Clone your project repository:
```bash
git clone https://github.com/your-username/java-nonogram-game.git
```

3. Navigate to the project directory:
```bash
cd java-nonogram-game
```

4. Compile the Java application:
```bash
javac NonogramGame.java
```

5. Start the application:
```bash
java NonogramGame
```

6. To keep the application running after closing the SSH session, use nohup:
```bash
nohup java NonogramGame &
```

## Accessing the application

1. Ensure your EC2 security group allows inbound traffic on the port your application is using (e.g., 8080).

2. Access your application using a web browser:
```
http://<instance-public-ip>:8080
```

Your Java Nonogram Game is now deployed on AWS EC2 and accessible via the internet.
