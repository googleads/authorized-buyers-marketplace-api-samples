#!/usr/bin/python
#
# Copyright 2021 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Patches a preferred deal at the given revision number.

This will modify the deal's flightStartTime, flightEndTime, and
preferredDealTerms.

Note: if the revision number is lower than what is stored for the proposal
server-side, the operation will be deemed obsolete and an error will be
returned. The revision number can be found at the proposal level.
"""


import argparse
import datetime
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../../..'))

from googleapiclient.errors import HttpError

import util


_DEALS_NAME_TEMPLATE = 'buyers/%s/proposals/%s/deals/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_PROPOSAL_RESOURCE_ID = 'ENTER_PROPOSAL_RESOURCE_ID_HERE'
DEFAULT_DEAL_RESOURCE_ID = 'ENTER_DEAL_RESOURCE_ID_HERE'


def main(marketplace, args):
    deal_name = _DEALS_NAME_TEMPLATE % (
        args.account_id, args.proposal_id, args.deal_id)

    flight_start_time = (datetime.datetime.now(datetime.timezone.utc) +
                         datetime.timedelta(days=2))
    flight_end_time = flight_start_time + datetime.timedelta(days=1)

    body = {
        'proposalRevision': args.proposal_revision,
        'flightStartTime': flight_start_time.isoformat(),
        'flightEndTime': flight_end_time.isoformat(),
        'preferredDealTerms': {
            'fixedPrice': {
                'amount': {
                    'units': args.fixed_price_units,
                    'nanos': args.fixed_price_nanos
                }
            }
        }
    }

    update_mask = ('flightStartTime,flightEndTime,'
                   'preferredDealTerms.fixedPrice.amount.units,'
                   'preferredDealTerms.fixedPrice.amount.nanos')

    print(f'Patching preferred deal with name "{deal_name}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().proposals().deals().patch(
            name=deal_name, body=body, updateMask=update_mask).execute()
    except HttpError as e:
        print(e)
        sys.exit(1)

    pprint.pprint(response)


if __name__ == '__main__':
    try:
        service = util.get_service(version='v1')
    except IOError as ex:
        print(f'Unable to create marketplace service - {ex}')
        print('Did you specify the key file in util.py?')
        sys.exit(1)

    parser = argparse.ArgumentParser(
        description='Patch a preferred deal at the given revision number.')
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the parent '
              'proposal was created. This will be used to construct the '
              'name used as a path parameter for the deals.patch request.'))
    parser.add_argument(
        '-p', '--proposal_id', default=DEFAULT_PROPOSAL_RESOURCE_ID,
        help=('The resource ID of the buyers.proposals resource for which the '
              'deal was created. This will be used to construct the '
              'name used as a path parameter for the deals.patch request.'))
    parser.add_argument(
        '-d', '--deal_id', default=DEFAULT_DEAL_RESOURCE_ID,
        help=('The resource ID of the buyers.proposals.deals resource that is '
              'being patched. This will be used to construct the name used as '
              'a path parameter for the deals.patch request.'))
    parser.add_argument(
        '-r', '--proposal_revision', required=True,
        help=('The revision number for the proposal associated with the deal '
              'being modified. Each update to the proposal or its deals causes '
              'the number to increment. The revision number specified must '
              'match the value stored server-side in order for the operation '
              'to be performed.'))
    # Optional fields.
    parser.add_argument(
        '-u', '--fixed_price_units', default=1,
        help='Whole units of the currency specified for the preferred deal.')
    parser.add_argument(
        '-n', '--fixed_price_nanos', default=500000000,
        help=('Number of nano units of the currency specified for the '
              'preferred deal.'))
    main(service, parser.parse_args())
