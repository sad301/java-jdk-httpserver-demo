<?php

use DI\Container;
use Slim\Factory\AppFactory;
use Slim\Views\Twig;

require __DIR__ . "/../lib/vendor/autoload.php";
require __DIR__ . "/../lib/controller/JenisNomorController.php";
require __DIR__ . "/../lib/controller/PegawaiController.php";
require __DIR__ . "/../lib/controller/RootController.php";
require __DIR__ . "/../lib/dao/JenisNomorDAO.php";
require __DIR__ . "/../lib/UiController.php";
require __DIR__ . "/../lib/util.php";

$container = new Container();
$container->set("database", function () {
    return new SQLite3(__DIR__ . "/../database/absensi.db");
});
$container->set("jenis_nomor_dao", function ($c) {
    return new JenisNomorDAO($c->get("database"));
});
$container->set("view", function () {
    return Twig::create(__DIR__ . "/../templates", ["cache" => false]);
});
$container->set("util", function () {
    return new Util();
});

AppFactory::setContainer($container);
$app = AppFactory::create();
$app->addErrorMiddleware(true, true, true);
$app->get("/", \UiController::class . ":index");
$app->get("/home", \UiController::class . ":home");
$app->get("/pegawai", \UiController::class . ":pegawai");
$app->get("/api", \RootController::class);
$app->get("/api/jenis_nomor", \JenisNomorController::class . ":retrieve");
$app->get("/api/pegawai", \PegawaiController::class . ":retrieve");
$app->post("/api/jenis_nomor", \JenisNomorController::class . ":create");
$app->run();

?>
