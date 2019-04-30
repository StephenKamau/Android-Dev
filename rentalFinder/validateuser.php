<?php
require "connection.php";
function userExists($username)
{
    $query = "SELECT emailaddress FROM users WHERE emailaddress = ?";
    global $con;
    if ($stmt = $con->prepare($query)) {
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch();
        if ($stmt->num_rows == 1) {
            $stmt->close();
            return true;
        }
        $stmt->close();
    }

    return false;
}