<?php
require "connection.php";
$filepath = 'http://10.2.22.47:86/dev/rentalfinder/';
$UPLOAD_DIR = "images/";

//check if data is posted
if ($_POST) {

    //assign recieved values to variables
    $apartmentname = mysqli_real_escape_string($con, $_POST['apartment']);
    $location = mysqli_real_escape_string($con, $_POST['location']);
    $description = mysqli_real_escape_string($con, $_POST['description']);
    $rentpermonth = mysqli_real_escape_string($con, $_POST['rent_per_month']);
    $principalcontact = mysqli_real_escape_string($con, $_POST['principal_contact']);

    $apartmentImage = $_POST['apartmentImage'];

   //check box handler
    if (isset($_POST['status']) && $_POST['status'] == "true") {
        $status = $_POST['status'];
    } else {
        $status = "false";
    }

    //decoding and storing the image path
    // requires php5
    $img = $apartmentImage;
    $img = str_replace('data:image/png;base64,', '', $img);
    $img = str_replace(' ', '+', $img);
    $data = base64_decode($img);
    $file = $UPLOAD_DIR . uniqid() . '.png';

    //sql
    $sql = "INSERT INTO apartments(apartmentname,location,description,status,rentpermonth,principalcontact,imageurl)
    VALUES('$apartmentname','$location','$description','$status','$rentpermonth','$principalcontact','$file')";
    $query = mysqli_query($con, $sql);

    //insert the records
    if ($query) {
        echo "Record created successfully ";
        $success = file_put_contents($file, $data);
    } else {
        echo "Error: " . mysqli_error($con);
    }
}
