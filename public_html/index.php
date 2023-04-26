<?php

use DI\Container;
use Slim\Factory\AppFactory;
use Slim\Views\Twig;

require __DIR__ . "/../lib/vendor/autoload.php";
require "UiController.php";

$container = new Container();
$container->set("database", function () {
    return new SQLite3(__DIR__ . "/../database/absensi.db");
});
$container->set("view", function () {
    return Twig::create(__DIR__ . "/../templates", ["cache" => false]);
});

AppFactory::setContainer($container);
$app = AppFactory::create();
$app->addErrorMiddleware(true, true, true);
$app->get("/", \UiController::class . ":index");
$app->get("/home", \UiController::class . ":home");
$app->get("/pegawai", \UiController::class . ":pegawai");
$app->get("/api", function ($req, $res) {
    $res->getBody()->write(json_encode(["message" => "api index, nothing to see here"]));
    return $res->withHeader("Content-Type", "application/json");
});
$app->get("/api/pegawai", function ($req, $res) {
    $res->getBody()->write(json_encode(["message" => "/api/pegawai"]));
    return $res->withHeader("Content-Type", "application/json");
});
$app->run();

?>
