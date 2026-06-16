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
use Google\Service\AuthorizedBuyersMarketplace\UnsubscribeClientsRequest;

/**
 * This example shows how to unsubscribe a given set of clients from a specified auction package.
 */
class UnsubscribeClientsFromAuctionPackages extends BaseExample
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
                    'unsubscribed from an auction package. This will be used to construct the ' .
                    'name used as a path parameter for the auctionPackages.unsubscribeClients ' .
                    'request.',
                'required' => true
            ],
            [
                'name' => 'auction_package_id',
                'display' => 'Auction Package ID',
                'description' =>
                    'The resource ID of the buyers.auctionPackages resource that the buyer will ' .
                    'unsubscribe one or more of its clients from. This will be used to ' .
                    'construct the name used as a path parameter for the ' .
                    'auctionPackages.unsubscribeClients request.',
                'required' => true
            ],
            [
                'name' => 'client_ids',
                'display' => 'Client IDs',
                'description' =>
                    'The resource IDs of the buyers.clients resources that are to be ' .
                    'unsubscribed from an auction package. Specify each client ID separated by ' .
                    'a comma. These will be used to construct the client names included in the ' .
                    'auctionPackages.unsubscribeClients request body.',
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
        $unsubscribeClientsRequest = new UnsubscribeClientsRequest();
        $unsubscribeClientsRequest->clients = array_map(
            function ($clientId) use ($parent) {
                return "$parent/clients/$clientId";
            },
            $values["client_ids"]
        );

        print "<h2>Unsubscribing clients from auction package '$auctionPackage' on behalf of " .
              "buyer account w/ ID '$accountId':</h2>";
        $result = $this->service->buyers_auctionPackages->unsubscribeClients(
            $auctionPackage,
            $unsubscribeClientsRequest
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Unsubscribe Clients from Auction Package';
    }
}
