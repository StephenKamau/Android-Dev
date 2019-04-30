<?php
require "connection.php";

//Getting the page number which is to be displayed  
$page = $_GET['page'];

//Initially we show the data from 1st row that means the 0th row 
$start = 0; 
 
//Limit is 3 that means we will show 3 items at once
$limit = 5;

//Counting the total item available in the database 
$total = mysqli_num_rows(mysqli_query($con, "SELECT id from apartments"));
 
//We can go atmost to page number total/limit
$page_limit = ($total / $limit);

$json = "";

//If the page number is more than the limit we cannot show anything 
if ($page <= $page_limit) {
 
    //Calculating start for every given page number 
    $start = ($page - 1) * $limit;

    $sql = "SELECT apartmentname,location,description,rentpermonth,principalcontact,imageurl FROM  apartments LIMIT $start,$limit";
    $query = mysqli_query($con, $sql);

    //check whether the table has data
    if (mysqli_num_rows($query) > 0) {
        while ($row[] = $query->fetch_assoc()) {
            $tem = $row;
            $json = json_encode($tem);
        }
        echo $json;
    }
}