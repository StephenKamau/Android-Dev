<?php
$username = "root";
$password = "";
$host = "localhost";
$database = "rental";

$con = mysqli_connect($host, $username, $password, $database);

//check if the connection is working
if (!$con) {
    die("Error connecting to the database: " . mysqli_connect_error($con));
}