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
use Google\Service\AuthorizedBuyersMarketplace\UnsubscribeAuctionPackageRequest;

/**
 * This example illustrates how to unsubscribe a given buyer from a specified auction package.
 */
class UnsubscribeFromAuctionPackages extends BaseExample
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
                    'The resource ID of the buyers resource that is to unsubscribe from an ' .
                    'auction package. This will be used to construct the name used as a path ' .
                    'parameter for the auctionPackages.unsubscribe request.',
                'required' => true
            ],
            [
                'name' => 'auction_package_id',
                'display' => 'Auction Package ID',
                'description' =>
                    'The resource ID of the buyers.auctionPackages resource that the buyer will ' .
                    'unsubscribe from. This will be used to construct the name used as a path ' .
                    'parameter for the auctionPackages.unsubscribe request.',
                'required' => true
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/auctionPackages/$values[auction_package_id]";

        $unsubscribeRequest = new UnsubscribeAuctionPackageRequest();

        print "<h2>Unsubscribing from Auction Package with name '$name':</h2>";
        $result = $this->service->buyers_auctionPackages->unsubscribe($name, $unsubscribeRequest);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Unsubscribe from Auction Package';
    }
}
