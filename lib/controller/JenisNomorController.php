<?php

use Psr\Container\ContainerInterface;

class JenisNomorController {

    private $container;

    public function __construct(ContainerInterface $container) {
        $this->container = $container;
    }

    public function create($req, $res) {
        if ($req->getHeader("Content-Type")[0] != "application/json") {
            return $res->withStatus(400);
        }
        $jenisNomor = json_decode(file_get_contents("php://input"), true);
        if (json_last_error())
        $res->getBody()->write(json_encode(["message" => "done!"]));
        return $res->withHeader("Content-Type", "application/json");
    }

    public function retrieve($req, $res) {
        $res->getBody()->write($this->container->get("jenis_nomor_dao")->retrieve());
        return $res->withHeader("Content-Type", "application/json");
    }

    public function delete($req, $res, $args) {

    }
}

?>