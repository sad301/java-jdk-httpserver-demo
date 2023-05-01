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
        if (json_last_error() != JSON_ERROR_NONE) {
            $res->getBody()->write(json_encode(["message" => json_last_error_msg()]));
            return $res->withStatus(500);
        }
        if (!$this->container->get("util")->array_keys_exists(["id", "deskripsi"], $jenisNomor)) {
            $res->getBody()->write(json_encode(["message" => "incomplete fields"]));
            return $res->withHeader("Content-Type", "application/json")->withStatus(400);
        }
        $result = $this->container->get("jenis_nomor_dao")->create($jenisNomor);
        if (!$result) {
            $res->getBody()->write(json_encode(["message" => $this->container->get("jenis_nomor_dao")->errorMessage]));
            return $res->withStatus(500);
        }
        $res->getBody()->write(json_encode(["message" => "done!"]));
        return $res->withHeader("Content-Type", "application/json");
    }

    public function retrieve($req, $res) {
        $dao = $this->container->get("dao")["jenis_nomor"];
        $res->getBody()->write($dao->retrieve());
        return $res->withHeader("Content-Type", "application/json");
    }

    public function delete($req, $res, $args) {
        $dao = $this->container->get("dao")["jenis_nomor"];
        $result = $dao->delete(["id" => $args["id"]]);
        if (!$result) {
            $res->getBody()->write(json_encode(["message" => $dao->errorMessage]));
            return $res->withHeader("Content-Type", "application/json")->withStatus(500);
        }
        return $res->withStatus(204);
    }
}

?>