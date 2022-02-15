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

"""Gets a single publisher profile for the given account and profile IDs."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_PUB_PROFILE_NAME_TEMPLATE = 'buyers/%s/publisherProfiles/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_PUB_PROFILE_RESOURCE_ID = 'ENTER_PUB_PROFILE_RESOURCE_ID_HERE'


def main(marketplace, args):
    account_id = args.account_id
    publisher_profile_id = args.publisher_profile_id

    print(f'Get publisher profile "{publisher_profile_id}" for account '
          f'"{account_id}":')
    try:
        # Construct and execute the request.
        response = marketplace.buyers().publisherProfiles().get(
            name=_PUB_PROFILE_NAME_TEMPLATE % (
                account_id, publisher_profile_id)).execute()
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
        description=('Get a publisher profile for the given buyer account ID '
                     'and publisher profile ID.'))
    # Required fields.
    parser.add_argument(
        '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
        help=('The resource ID of the buyers resource under which the '
              'publisherProfiles resource is being accessed. This will be used '
              'to construct the name used as a path parameter for the '
              'publisherProfiles.get request.'))
    parser.add_argument(
        '-p', '--publisher_profile_id', default=DEFAULT_PUB_PROFILE_RESOURCE_ID,
        help=('The resource ID of the buyers.publisherProfiles resource that '
              'is being accessed. This will be used to construct the name used '
              'as a path parameter for the publisherProfiles.get request.'))

    main(service, parser.parse_args())
