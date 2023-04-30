<?php

class Util {
    public function array_keys_exists(array $keys, array $array) {
        return !array_diff($keys, array_keys($array));
    }
}

?>