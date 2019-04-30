<?php
require "connection.php";
require "validateuser.php";

//check if data is posted
if ($_POST) {

    //assign recieved values to variables
    $firstname = mysqli_real_escape_string($con, $_POST['first_name']);
    $lastname = mysqli_real_escape_string($con, $_POST['last_name']);
    $emailaddress = mysqli_real_escape_string($con, $_POST['email']);
    $phone = mysqli_real_escape_string($con, $_POST['phone']);
    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
    $gender = $_POST['gender'];
    $dateofbirth = $_POST['date_of_birth'];

    if (!userExists($emailaddress)) {
        //sql
        $sql = "INSERT INTO users(firstname,lastname,emailaddress,phone,password,gender,dateofbirth) VALUES('$firstname','$lastname','$emailaddress','$phone','$password','$gender','$dateofbirth')";
        $query = mysqli_query($con, $sql);

        //insert the records
        if ($query) {
            echo "Record created successfully ";
        } else {
            echo "Error: " . mysqli_error($con);
        }
    } else {
        echo "Email already registered";
    }
}
