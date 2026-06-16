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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_AuctionPackages;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\SubscribeClientsRequest;

/**
 * This example illustrates how to subscribe a given set of clients to a specified auction package.
 *
 * Note that the specified buyer account will also be subscribed to the auction package if it
 * hasn't been already.
 */
class SubscribeClientsToAuctionPackages extends BaseExample
{
    public function __construct($client)
    {
        $this->service = Config::getGoogleServiceAuthorizedBuyersMarketplace($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'account_id',
                'display' => 'Account ID',
                'description' =>
                    'The resource ID of the buyers resource that is to have its clients ' .
                    'subscribed to an auction package. This will be used to construct the name ' .
                    'used as a path parameter for the auctionPackages.subscribeClients request.',
                'required' => true
            ],
            [
                'name' => 'auction_package_id',
                'display' => 'Auction Package ID',
                'description' =>
                    'The resource ID of the buyers.auctionPackages resource that the buyer will ' .
                    'subscribe one or more of its clients to. This will be used to construct ' .
                    'the name used as a path parameter for the auctionPackages.subscribeClients ' .
                    'request.',
                'required' => true
            ],
            [
                'name' => 'client_ids',
                'display' => 'Client IDs',
                'description' =>
                    'The resource IDs of the buyers.clients resources that are to be subscribed ' .
                    'to the auction package. Specify each client ID separated by a comma. These ' .
                    'will be used to construct the client names included in the ' .
                    'auctionPackages.subscribeClients request body.',
                'required' => true,
                'is_array' => true
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $accountId = $values["account_id"];
        $parent = "buyers/$accountId";
        $auctionPackage = "$parent/auctionPackages/$values[auction_package_id]";
        $subscribeClientsRequest = new SubscribeClientsRequest();
        $subscribeClientsRequest->clients = array_map(
            function ($clientId) use ($parent) {
                return "$parent/clients/$clientId";
            },
            $values["client_ids"]
        );

        print "<h2>Subscribing clients to auction package '$auctionPackage' on behalf of buyer " .
              "account w/ ID '$accountId':</h2>";
        $result = $this->service->buyers_auctionPackages->subscribeClients(
            $auctionPackage,
            $subscribeClientsRequest
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Subscribe Clients to Auction Package';
    }
}
