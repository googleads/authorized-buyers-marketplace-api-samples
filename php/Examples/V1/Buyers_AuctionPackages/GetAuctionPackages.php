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

/**
 * This example shows how to get an auction package for the given account and auction package IDs.
 */
class GetAuctionPackages extends BaseExample
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
                'required' => true,
                'description' =>
                    'The resource ID of the buyers resource under which the auction package is ' .
                    'being accessed. This will be used to construct the name used as a path ' .
                    'parameter for the auctionPackages.get request.'
            ],
            [
                'name' => 'auction_package_id',
                'display' => 'Auction package ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers.auctionPackages resource that is being ' .
                    'accessed. This will be used to construct the name used as a path ' .
                    'parameter for the auctionPackages.get request.'
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

        try {
            $client = $this->service->buyers_auctionPackages->get($name);
            print '<h2>Found auction package.</h2>';
            $this->printResult($client);
        } catch (Google_Service_Exception $ex) {
            if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                print '<h1>Auction package not found or can\'t access auction package.</h1>';
            } else {
                throw $ex;
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Get Auction Package';
    }
}
