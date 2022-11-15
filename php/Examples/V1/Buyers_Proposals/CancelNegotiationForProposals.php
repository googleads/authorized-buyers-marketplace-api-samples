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
use Google\Service\AuthorizedBuyersMarketplace\CancelNegotiationRequest;

/**
 * This example illustrates how to cancel the ongoing negotiation for the specified proposal.
 *
 * This method is not supported for proposals including private auction deals because negotiaton
 * for that deal type can not be canceled. On successful cancelation, the proposal's state will be
 * set to TERMINATED.
 *
 * This does not cancel or end serving for deals that have already been finalized. For finalized
 * deals that are under renegotiation, calling this method will instead reset the proposal's state
 * to FINALIZED.
 */
class CancelNegotiationForProposals extends BaseExample
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
                    'for the proposals.cancelNegotiation request.',
                'required' => true
            ],
            [
                'name' => 'proposal_id',
                'display' => 'Proposal ID',
                'description' =>
                    'The resource ID of the buyers.proposals resource for which the proposal ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the proposals.cancelNegotiation request.',
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

        $cancelNegotiationRequest = new CancelNegotiationRequest();

        print "<h2>Canceling negotiation for proposal '$name':</h2>";

        $result = $this->service->buyers_proposals->cancelNegotiation(
            $name,
            $cancelNegotiationRequest
        );

        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Cancel Negotiation for Proposal';
    }
}
