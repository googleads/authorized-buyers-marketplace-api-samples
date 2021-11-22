#!/usr/bin/python
#
# Copyright 2021 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Utilities used by the Authorized Buyers Marketplace API Samples."""


import os

from googleapiclient.discovery import build
from googleapiclient.discovery import build_from_document
from google.auth.transport.requests import AuthorizedSession
from google.oauth2 import service_account


# Update these with the values for your Service Account found in the Google
# Developers Console.
KEY_FILE = 'INSERT_PATH_TO_KEY_FILE'  # Path to Service Account JSON key file.
# The maximum number of results to be returned in a page for any list response.
MAX_PAGE_SIZE = 50
# Authorized Buyers Marketplace API authorization scope.
_SCOPE = 'https://www.googleapis.com/auth/authorized-buyers-marketplace'
_MARKETPLACE_API_NAME = 'authorizedbuyersmarketplace'
_MARKETPLACE_VERSIONS = ['v1']
_DEFAULT_VERSION = _MARKETPLACE_VERSIONS[-1]

_DEFAULT_DISCOVERY_PATH = os.path.join(
    os.path.os.path.dirname(os.path.realpath(__file__)), 'discovery.json')


def _get_credentials():
    """Steps through Service Account OAuth 2.0 flow to retrieve credentials."""
    return service_account.Credentials.from_service_account_file(
        KEY_FILE, scopes=[_SCOPE]
    )


def download_discovery_document(api_name, version,
                                path=_DEFAULT_DISCOVERY_PATH, label=None):
    """Downloads a discovery document for the given api_name and version.

    This utility assumes that the API for which a discovery document is being
    retrieved is publicly accessible. However, you may access allowlisted
    resources for a public API if you are added to its allowlist and specify
    the associated label.

    Args:
        api_name: a str indicating the name of the API for which a discovery
            document is to be downloaded.
        version: a str indicating the version number of the API.
        path: a str indicating the path to which you want to save the
            downloaded discovery document.
        label: a str indicating a label to be applied to the discovery service
            request. This is not applicable when downloading the discovery
            document of a legacy API. For non-legacy APIs, this may be used as
            a means of programmatically retrieving a copy of a discovery
            document containing allowlisted content.

    Raises:
        ValueError: If either the specified API name and version can't be found
            by the discovery service, or if downloading the discovery document fails.
    """
    credentials = _get_credentials()
    auth_session = AuthorizedSession(credentials)
    discovery_service = build('discovery', 'v1')
    discovery_rest_url = None

    discovery_response = discovery_service.apis().list(name=api_name).execute()

    if 'items' in discovery_response:
        for api in discovery_response['items']:
            if api['version'] == version:
                discovery_rest_url = api['discoveryRestUrl']
                break

    if discovery_rest_url:
        if label:
            # Apply the label query parameter if it exists.
            path_params = f'&labels={label}'
            discovery_rest_url += path_params

        discovery_response = auth_session.get(discovery_rest_url)

        if discovery_response.status_code == 200:
            with open(path, 'wb') as handler:
                handler.write(discovery_response.text)
        else:
            raise ValueError(
                'Unable to retrieve discovery document for api name '
                f'"{api_name}" and version "{version}" via discovery '
                f'URL: {discovery_rest_url}')
    else:
        raise ValueError(
            f'API with name "{api_name}" and version "{version}" was '
            'not found.')


def get_service(version=_DEFAULT_VERSION, developer_key=None):
    """Builds the realtimebidding service for the specified version.

    Args:
        version: a str indicating the Authorized Buyers Marketplace API version
            to be retrieved.
        developer_key: a str, also known as an API key, found in the Google
            Cloud Console. This optional field is used to access closed beta
            features.

    Returns:
        A googleapiclient.discovery.Resource instance used to interact with the
        Authorized Buyers Real-Time Bidding API.

    Raises:
        ValueError: raised if the specified version is not a valid version of
        the Authorized Buyers Marketplace API.
    """
    credentials = _get_credentials()

    if version in _MARKETPLACE_VERSIONS:
        # Initialize client for the Marketplace API.
        service = build(
            _MARKETPLACE_API_NAME,
            version,
            credentials=credentials,
            developerKey=developer_key)
    else:
        raise ValueError(
            'Invalid version provided. Supported versions are: '
            f'{", ".join(_MARKETPLACE_VERSIONS)}')

    return service


def get_service_from_file(discovery_file):
    """Builds a service using the specified discovery document.

    Args:
        discovery_file: a str path to the JSON discovery file for the service
        to be created.

    Returns:
        A googleapiclient.discovery.Resource instance used to interact with the
        service specified by the discovery_file.
    """
    credentials = _get_credentials()

    with open(discovery_file, 'r') as handler:
        discovery_doc = handler.read()

    service = build_from_document(
        service=discovery_doc, credentials=credentials)

    return service
