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

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\BatchUpdateDealsRequest;
use Google\Service\AuthorizedBuyersMarketplace\BatchUpdateDealsResponse;
use Google\Service\AuthorizedBuyersMarketplace\CriteriaTargeting;
use Google\Service\AuthorizedBuyersMarketplace\Deal;
use Google\Service\AuthorizedBuyersMarketplace\MarketplaceTargeting;
use Google\Service\AuthorizedBuyersMarketplace\UpdateDealRequest;
use Google\Service\Exception as GoogleServiceException;

/**
 * This example illustrates how to patch user list targeting for one or more of a proposal's deals.
 *
 * This operation requires that the deals all exist under the same proposal.
 *
 * The user list targeting of the given deals will be modified to target the specified user lists.
 * User lists can be retrieved via the Real-time Bidding API's buyers.userLists resource. You can
 * learn more about buyers.userLists in the reference documentation:
 * https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/v1/buyers.userLists
 *
 * Note: Only preferred and programmatic guaranteed deals can be modified by the buyer. Attempting
 * to modify a private auction deal will result in an error response.
 */
class BatchUpdateDeals extends BaseExample
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
                    'The resource ID of the buyers resource under which one or more deals are ' .
                    'being patched. This will be used to construct the name used as a path ' .
                    'parameter for the deals.batchUpdate request, and each deal name requested .',
                'required' => true
            ],
            [
                'name' => 'proposal_id',
                'display' => 'Proposal ID',
                'description' =>
                    'The resource ID of the buyers.proposals resource under which one or more ' .
                    'deals are being patched. This will be used to construct the name used as a ' .
                    'path parameter for the deals.batchUpdate request, and each deal name ' .
                    'included in the request body.',
                'required' => true
            ],
            [
                'name' => 'deal_ids',
                'display' => 'Deal IDs',
                'description' =>
                    'The resource ID of one or more buyers.proposals.deals resources that will ' .
                    'be patched in a batch update operation. These will be used to construct ' .
                    'the deal names included in the request body. Specify each user list ID ' .
                    'separated by a comma.',
                'required' => true,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'proposal_revision',
                'display' => 'Proposal revision',
                'description' =>
                    'The revision number for the corresponding proposal of the deals being ' .
                    'modified. Each update to the proposal or its deals causes the number to ' .
                    'increment. The revision number specified must match the value stored ' .
                    'server-side in order for the operation to be performed.',
                'required' => true
            ],
            [
                'name' => 'user_list_ids',
                'display' => 'User list IDs',
                'description' =>
                    'The resource ID of one or more buyers.userLists resources that are to be ' .
                    'targeted by the given deals. Specify each user list ID separated by a comma.',
                'required' => true,
                'is_array' => true,
                'default' => []
            ],
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $accountId = $values['account_id'];
        $proposalRevision = $values['proposal_revision'];

        $parent = "buyers/$accountId/proposals/$values[proposal_id]";

        $userListTargeting = new CriteriaTargeting();
        $userListTargeting->targetedCriteriaIds = $values['user_list_ids'];
        $marketplaceTargeting = new MarketplaceTargeting();
        $marketplaceTargeting->userListTargeting = $userListTargeting;

        $batchUpdateDealsRequest = new BatchUpdateDealsRequest();
        $batchUpdateDealsRequest->requests = array_map(
            function ($dealId) use ($parent, $proposalRevision, $marketplaceTargeting) {
                $deal = new Deal();
                $deal->name = "$parent/deals/$dealId";
                $deal->proposalRevision = $proposalRevision;
                $deal->targeting = $marketplaceTargeting;

                $updateDealRequest = new UpdateDealRequest();
                $updateDealRequest->deal = $deal;
                $updateDealRequest->updateMask = 'targeting.userListTargeting.targetedCriteriaIds';

                return $updateDealRequest;
            },
            $values['deal_ids']
        );

        print "<h2>Batch updating deals for Proposal with name '$parent':</h2>";

        try {
            $result = $this->service->buyers_proposals_deals->batchUpdate(
                $parent,
                $batchUpdateDealsRequest
            );
            $this->printResult($result);
        } catch (GoogleServiceException $ex) {
            print "<h3>Batch updating deals was unsuccessful.</h3>";
            $errorResponse = $ex->getMessage();
            print "<p>Error response:</br><code>$errorResponse</code></p>";
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Batch Update Deals';
    }
}
