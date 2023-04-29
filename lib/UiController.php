<?php

use Psr\Container\ContainerInterface;

class UiController {

    private $container;

    public function __construct(ContainerInterface $container) {
        $this->container = $container;
    }

    public function index($req, $res) {
        return $this->container->get("view")->render($res, "index.html");
    }

    public function home($req, $res) {
        return $this->container->get("view")->render($res, "home.html");
    }

    public function pegawai($req, $res) {
        return $this->container->get("view")->render($res, "pegawai.html");
    }
}

?>