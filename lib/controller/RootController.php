<?php

use Psr\Container\ContainerInterface;

class RootController {

    private $container;

    public function __construct(ContainerInterface $container) {
        $this->container = $container;
    }

    public function __invoke($req, $res) {
        $res->getBody()->write(json_encode(["message" => "api index, nothing to see here"]));
        return $res->withHeader("Content-Type", "application/json");
    }
}

?>