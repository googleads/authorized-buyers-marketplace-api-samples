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
use Google\Service\AuthorizedBuyersMarketplace\AddCreativeRequest;

/**
 * This example illustrates how to add a creative to a finalized deal that will be used in bids.
 *
 * It is recommended that those configuring programmatic guaranteed deals use this method to
 * associate at least one creative that is ready to be placed in bids with the deal before
 * signaling that the deal is ready to begin serving with finalizedDeals.setReadyToServe.
 *
 * A buyer's creatives can be viewed with the Real-time Bidding API:
 * https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.creatives
 */
class AddCreativeToFinalizedDeals extends BaseExample
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
                    'being accessed. This will be used to construct the name used as a path ' .
                    'parameter for the finalizedDeals.addCreative request.',
                'required' => true
            ],
            [
                'name' => 'creative_id',
                'display' => 'Creative ID',
                'description' =>
                    'The resource ID of the creatives resource that is to be added to the ' .
                    'finalized deal. This will be used to construct the name of the creative, ' .
                    'which will be included in the body of the finalizedDeals.addCreative ' .
                    'request.',
                'required' => true
            ],
            [
                'name' => 'deal_id',
                'display' => 'Deal ID',
                'description' =>
                    'The resource ID of the buyers.finalizedDeals resource that is being ' .
                    'accessed. Note that this will be identical to the resource ID of the ' .
                    'corresponding buyers.proposals.deals resource. This will be used to ' .
                    'construct the name used as a path parameter for the ' .
                    'finalizedDeals.addCreative request.',
                'required' => true,
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $buyerName = "buyers/$values[account_id]";
        $name = "$buyerName/finalizedDeals/$values[deal_id]";

        $addCreativeRequest = new AddCreativeRequest();
        $addCreativeRequest->creative = "$buyerName/creatives/$values[creative_id]";

        print "<h2>Adding creative to proposal with name '$name':</h2>";
        $result = $this->service->buyers_finalizedDeals->addCreative($name, $addCreativeRequest);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Add Creative to Finalized Deal';
    }
}
