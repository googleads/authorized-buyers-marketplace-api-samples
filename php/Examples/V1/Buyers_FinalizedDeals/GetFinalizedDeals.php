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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_FinalizedDeals;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\Exception as GoogleServiceException;

/**
 * This example illustrates how to get a single finalized deal for the given account and deal IDs.
 */
class GetFinalizedDeals extends BaseExample
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
                    'The resource ID of the buyers resource for which the finalized deal is ' .
                    'being accessed. This will be used to construct the name used as a path ' .
                    'parameter for the finalizedDeals.get request.'
            ],
            [
                'name' => 'deal_id',
                'display' => 'Deal ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers.finalizedDeals resource that is being ' .
                    'accessed. Note that this will be identical to the resource ID of the ' .
                    'corresponding buyers.proposals.deals resource. This will be used to ' .
                    'construct the name used as a path parameter for the finalizedDeals.get ' .
                    'request.'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/finalizedDeals/$values[deal_id]";

        try {
            $finalizedDeal = $this->service->buyers_finalizedDeals->get($name);
            print '<h2>Found finalized deal.</h2>';
            $this->printResult($finalizedDeal);
        } catch (GoogleServiceException $ex) {
            if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                print '<h1>Finalized deal not found or can\'t be accessed.</h1>';
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
        return 'Get Finalized Deal';
    }
}
