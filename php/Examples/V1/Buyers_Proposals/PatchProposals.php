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

namespace Google\Ads\AuthorizedBuyers\Marketplace\Examples\V1\Buyers_Proposals;

use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\Marketplace\ExampleUtil\Config;
use Google\Service\AuthorizedBuyersMarketplace\PrivateData;
use Google\Service\AuthorizedBuyersMarketplace\Proposal;
use Google\Service\Exception as GoogleServiceException;

/**
 * This example illustrates how to patch a specified proposal at the given revision number.
 *
 * Fields that can be patched for this resource can be found in the reference documentation:
 * https://developers.google.com/authorized-buyers/apis/marketplace/reference/rest/v1/buyers.proposals
 *
 * Note: If the revision number is lower than what is stored for the proposal server-side, the
 * operation will be deemed obsolete and an error will be returned.
 *
 * Only proposals for preferred and programmatic guaranteed deals can be modified by buyers.
 */
class PatchProposals extends BaseExample
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
                    'The resource ID of the buyers resource under which the proposal was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the proposals.patch request.',
                'required' => true
            ],
            [
                'name' => 'proposal_id',
                'display' => 'Proposal ID',
                'description' =>
                    'The resource ID of the buyers.proposals resource for which the proposal ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the proposals.patch request.',
                'required' => true
            ],
            [
                'name' => 'proposal_revision',
                'display' => 'Proposal revision',
                'description' =>
                    'The revision number for the proposal being modified. Each update to the ' .
                    'proposal or its deals causes the number to increment. The revision number ' .
                    'specified must match the value stored server-side in order for the ' .
                    'operation to be performed.',
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

        $name = "buyers/$values[account_id]/proposals/$values[proposal_id]";

        $privateData = new PrivateData();
        $privateData->referenceId = 'Marketplace-PHP-Sample-Reference-' . uniqid();

        $patchedProposal = new Proposal();
        $patchedProposal->proposalRevision = $values['proposal_revision'];
        $patchedProposal->buyerPrivateData = $privateData;

        $queryParams = ['updateMask' => 'buyerPrivateData.referenceId'];

        print "<h2>Patching Proposal with name '$name':</h2>";

        $this->printResult($result);

        try {
            $result = $this->service->buyers_proposals->patch(
                $name,
                $patchedProposal,
                $queryParams
            );
            $this->printResult($result);
        } catch (GoogleServiceException $ex) {
            print "<h3>Patching proposal was unsuccessful.</h3>";
            $errorResponse = $ex->getMessage();
            print "<p>Error response:</br><code>$errorResponse</code></p>";
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Patch Proposal';
    }
}
