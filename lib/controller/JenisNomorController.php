<?php

use Psr\Container\ContainerInterface;

class JenisNomorController {

    private $container;

    public function __construct(ContainerInterface $container) {
        $this->container = $container;
    }

    public function create($req, $res) {
        $res->getBody()->write(json_encode(["message" => "done!"]));
        return $res->withHeader("Content-Type", "application/json");
    }

    public function retrieve($req, $res) {
        $res->getBody()->write($this->container->get("jenis_nomor_dao")->retrieve());
        return $res->withHeader("Content-Type", "application/json");
    }
}

?>