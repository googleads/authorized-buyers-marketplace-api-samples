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
use Google\Service\AuthorizedBuyersMarketplace\SetReadyToServeRequest;
use Google\Service\Exception as GoogleServiceException;

/**
 * This example illustrates how to signal that a given finalized deal is ready to serve.
 *
 * By default, deals are set ready to serve as soon as they're finalized. For programmatic
 * guaranteed deals, bidders can opt out of this feature by asking their account manager.
 * This is recommended for programmatic guaranteed deals in order to ensure that bidders have
 * creatives prepared to be used in placing bids once the deal is serving. Use
 * finalizedDeals.addCreative to associate creatives with a programmatic guaranteed deal.
 */
class SetReadyToServeFinalizedDeals extends BaseExample
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
                    'The resource ID of the buyers resource for which the finalized deal is ' .
                    'being set ready to serve. This will be used to construct the name used as ' .
                    'a path parameter for the finalizedDeals.setReadyToServe request.',
                'required' => true
            ],
            [
                'name' => 'deal_id',
                'display' => 'Deal ID',
                'description' =>
                    'The resource ID of the buyers.finalizedDeals resource that is being ' .
                    'set ready to serve. This will be used to construct the name used as a path ' .
                    'parameter for the finalizedDeals.setReadyToServe request.',
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

        $name = "buyers/$values[account_id]/finalizedDeals/$values[deal_id]";

        print "<h2>Setting finalized deal with name '$name' as ready to serve:</h2>";
        $result = $this->service->buyers_finalizedDeals->setReadyToServe(
            $name,
            new SetReadyToServeRequest()
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Set Finalized Deal as Ready to Serve';
    }
}
