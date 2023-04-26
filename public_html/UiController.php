<?php

use Psr\Container\ContainerInterface;

class UiController {

    private $container;

    public function __construct(ContainerInterface $container) {
        $this->container = $container;
    }

    public function index($req, $res) {
        return $this->container->get("view")->render($res, "index.twig");
    }

    public function home($req, $res) {
        return $this->container->get("view")->render($res, "home.twig");
    }

    public function pegawai($req, $res) {
        return $this->container->get("view")->render($res, "pegawai.twig");
    }
}

?>