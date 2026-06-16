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

/**
 * This example illustrates how to list finalized deals for the given buyers, and their clients.
 */
class ListFinalizedDeals extends BaseExample
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
                'display' => 'Buyer account ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers resource for which the finalized deals are ' .
                    'being retrieved. This will be used to construct the parent used as a path ' .
                    'parameter for the finalizedDeals.list request.'
            ],
            [
                'name' => 'filter',
                'display' => 'Filter',
                'required' => false,
                'description' =>
                    'Query string to filter finalized deals. By default, this example will ' .
                    'filter by deal type to retrieve finalized deals including programmatic ' .
                    'guaranteed deals to demonstrate usage.',
                'default' => 'deal.dealType = PROGRAMMATIC_GUARANTEED'
            ],
            [
                'name' => 'order_by',
                'display' => 'Order by',
                'required' => false,
                'description' =>
                    'Query string used to sort the response of the list method. By default, ' .
                    'this will return deals in descending order of their flight start time to ' .
                    'demonstrate usage. To learn more about the syntax for this parameter, see: ' .
                    'https://cloud.google.com/apis/design/design_patterns#sorting_order',
                'default' => 'deal.flightStartTime desc'
            ],
            [
                'name' => 'page_size',
                'display' => 'Page size',
                'required' => false,
                'description' =>
                    'The number of rows to return per page. The server may return fewer rows ' .
                    'than specified. By default, a page size of 10 will be set.',
                'default' => 10
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $parentName = "buyers/$values[account_id]";

        $queryParams = [
            'filter' => $values['filter'],
            'orderBy' => $values['order_by'],
            'pageSize' => $values['page_size'],
        ];

        $result = $this->service->buyers_finalizedDeals->listBuyersFinalizedDeals(
            $parentName,
            $queryParams
        );

        print "<h2>Finalized deals found for '$parentName':</h2>";
        if (empty($result['finalizedDeals'])) {
            print '<p>No Finalized Deals found</p>';
        } else {
            foreach ($result['finalizedDeals'] as $finalizedDeal) {
                $this->printResult($finalizedDeal);
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'List Finalized Deals';
    }
}
