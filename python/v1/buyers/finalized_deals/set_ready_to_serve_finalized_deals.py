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

"""Signals that the given finalized deal is ready to serve.

By default, deals are set ready to serve as soon as they're finalized. For
programmatic guaranteed deals, bidders can opt out of this feature by asking
their account manager. This is recommended for programmatic guaranteed deals in
order to ensure that bidders have creatives prepared to be used in placing bids
once the deal is serving. Use finalizedDeals.addCreative to associate creatives
with a programmatic guaranteed deal.
"""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_FINALIZED_DEALS_NAME_TEMPLATE = 'buyers/%s/finalizedDeals/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_FINALIZED_DEAL_RESOURCE_ID = 'ENTER_DEAL_RESOURCE_ID_HERE'


def main(marketplace, args):
    finalized_deal_name = _FINALIZED_DEALS_NAME_TEMPLATE % (
        args.account_id, args.deal_id)

    print(f'Setting finalized deal with name "{finalized_deal_name}" as ready '
          'to serve:')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().finalizedDeals().setReadyToServe(
            deal=finalized_deal_name).execute()
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
        description='Signals that a finalized deal is ready to serve.')
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource for which the finalized '
              'deal is being set ready to serve. This will be used to '
              'construct the name used as a path parameter for the '
              'finalizedDeals.setReadyToServe request.'))
    parser.add_argument(
        '-d', '--deal_id', default=DEFAULT_FINALIZED_DEAL_RESOURCE_ID,
        help=('The resource ID of the buyers.finalizedDeals resource that is '
              'being set ready to serve. This will be used to construct the '
              'name used as a path parameter for the '
              'finalizedDeals.setReadyToServe request.'))

    main(service, parser.parse_args())
