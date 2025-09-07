<?php
require_once 'init.php';
$getalldishes = "select * from notifications";
$result = mysqli_query($con, $getalldishes);
$return_array = array();
while ($row = mysqli_fetch_assoc($result)) {
// create an associative array for each row of the result
// returned by the query
// call it row_array, then push it into the main array
// called return_Array
$row_array['id'] = $row['id'];
// left side indexes are own choice,
// right side indexes should be the attributes of the table
$row_array['description'] = $row['description'];
$row_array['image'] = $row['image'];
$row_array['date'] = $row['date'];
array_push($return_array, $row_array);
}
echo json_encode($return_array);
?>