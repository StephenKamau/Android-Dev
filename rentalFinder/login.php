<?php
require "connection.php";

//check if data is posted
if ($_POST) {
    $emailaddress = $_POST['email_address'];
    $password = $_POST['password'];


    //check if the email is in the system
    $sql = "SELECT * FROM users WHERE emailaddress = '$emailaddress'";
    $query = mysqli_query($con, $sql);
    
    //
    if (mysqli_num_rows($query) > 0) {
        while ($row = $query->fetch_assoc()) {
            $tempPassword = $row['password'];
            $username = $row['firstname'] . " " . $row['lastname'];
        }

        if (password_verify($password, $tempPassword)) {
            echo $username;
        } else {
            echo "fail";
        }
    } else {
        echo "fail";
    }
}