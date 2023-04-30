<?php

$txt = "{\"id\":\"001\",\"nama\":\"contoh\"}";
$data = json_decode($txt, true);
$diff = array_diff(["id", "nama"], array_keys($data));
if ($diff) {
    echo("not empty");
}
else {
    echo("is empty");
}

?>