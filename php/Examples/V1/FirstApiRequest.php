<?php

/**
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Sample application that authenticates and makes an API request.
 */

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1;

/**
 * Provide path to client library. See README.md for details.
 */
require_once __DIR__ . '/../../vendor/autoload.php';

use Google_Client;
use Google_Service_AuthorizedBuyersMarketplace;


session_start();

/**
 * You can retrieve this file from the Google Developers Console.
 *
 * See README.md for details.
 */
$keyFileLocation = "INSERT_PATH_TO_JSON_KEYFILE";

/**
 * Name of the buyer resource for which the API call is being made.
 */
$buyerName = "INSERT_BUYER_RESOURCE_NAME";

if ($keyFileLocation === 'INSERT_PATH_TO_JSON_KEYFILE') {
    print "WARNING: Authorization details not provided!\n";
    exit(1);
}

$client = new Google_Client();
$client->setApplicationName('Authorized Buyers Marketplace API PHP Samples');

$service = new Google_Service_AuthorizedBuyersMarketplace($client);

$client->setAuthConfig($keyFileLocation);
$client->addScope('https://www.googleapis.com/auth/authorized-buyers-marketplace');

if ($client->isAccessTokenExpired()) {
    $client->refreshTokenWithAssertion();
}

if ($client->getAccessToken()) {
    // Call the buyers.clients.list method to get a list of clients for the given buyer.
    $result = $service->buyers_clients->listBuyersClients($buyerName);

    print "Clients associated with buyer account\n";
    if (empty($result['clients'])) {
        print "No clients found\n";
        return;
    } else {
        foreach ($result['clients'] as $client) {
            print_r($client);
        }
    }
}
