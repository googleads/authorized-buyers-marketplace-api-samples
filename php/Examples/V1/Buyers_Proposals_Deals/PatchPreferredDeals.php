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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Proposals_Deals;

use DateInterval;
use DateTimeImmutable;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\Deal;
use Google\Service\AuthorizedBuyersMarketplace\Money;
use Google\Service\AuthorizedBuyersMarketplace\PreferredDealTerms;
use Google\Service\AuthorizedBuyersMarketplace\Price;
use Google\Service\Exception as GoogleServiceException;

/**
 * This example illustrates how to patch a specified deal at the given revision number.
 *
 * This will modify the deal's flightStartTime, flightEndTime, and preferredDealTerms.
 *
 * Note: If the revision number is lower than what is stored for the proposal server-side, the
 * operation will be deemed obsolete and an error will be returned. The revision number can be
 * found at the proposal level.
 */
class PatchPreferredDeals extends BaseExample
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
                    'The resource ID of the buyers resource under which the parent proposal was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the deals.patch request.',
                'required' => true
            ],
            [
                'name' => 'proposal_id',
                'display' => 'Proposal ID',
                'description' =>
                    'The resource ID of the buyers.proposals resource for which the deal was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the deals.patch request.',
                'required' => true
            ],
            [
                'name' => 'deal_id',
                'display' => 'Deal ID',
                'description' =>
                    'The resource ID of the buyers.proposals.deals resource that is being ' .
                    'patched. This will be used to construct the name used as a path parameter ' .
                    'for the deals.patch request.',
                'required' => true
            ],
            [
                'name' => 'proposal_revision',
                'display' => 'Proposal revision',
                'description' =>
                    'The revision number for the proposal associated with the deal being ' .
                    'modified. Each update to the proposal or its deals causes the number to ' .
                    'increment. The revision number specified must match the value stored ' .
                    'server-side in order for the operation to be performed.',
                'required' => true
            ],
            [
                'name' => 'fixed_price_units',
                'display' => 'Fixed price units',
                'description' => 'Whole units of the currency specified for the preferred deal.',
                'required' => false,
                'default' => 1
            ],
            [
                'name' => 'fixed_price_nanos',
                'display' => 'Fixed price nanos',
                'description' => 'Nano units of the currency specified for the preferred deal.',
                'required' => false,
                'default' => 500000000
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/proposals/$values[proposal_id]/deals/$values[deal_id]";

        // Patch new start and end flight times in RFC3339 "Zulu" format.
        $now = new DateTimeImmutable('now');
        $startDateTime = $now->add(new DateInterval('P1D'));
        $endDateTime = $startDateTime->add(new DateInterval('P1D'));

        $fixedPriceAmount = new Money();
        $fixedPriceAmount->units = $values['fixed_price_units'];
        $fixedPriceAmount->nanos = $values['fixed_price_nanos'];

        $fixedPrice = new Price();
        $fixedPrice->amount = $fixedPriceAmount;

        $preferredDealTerms = new PreferredDealTerms();
        $preferredDealTerms->fixedPrice = $fixedPrice;

        $patchedDeal = new Deal();
        $patchedDeal->proposalRevision = $values['proposal_revision'];
        $patchedDeal->flightStartTime = Config::getDateStringFromDateTimeImmutable($startDateTime);
        $patchedDeal->flightEndTime = Config::getDateStringFromDateTimeImmutable($endDateTime);
        $patchedDeal->preferredDealTerms = $preferredDealTerms;

        $queryParams = [
            'updateMask' =>
                'flightStartTime,flightEndTime,preferredDealTerms.fixedPrice.amount.units,' .
                'preferredDealTerms.fixedPrice.amount.nanos'
        ];

        print "<h2>Patching Deal with name '$name':</h2>";

        $this->printResult($result);

        try {
            $result = $this->service->buyers_proposals_deals->patch(
                $name,
                $patchedDeal,
                $queryParams
            );
            $this->printResult($result);
        } catch (GoogleServiceException $ex) {
            print "<h3>Patching deal was unsuccessful.</h3>";
            $errorResponse = $ex->getMessage();
            print "<p>Error response:</br><code>$errorResponse</code></p>";
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Patch Preferred Deals';
    }
}
