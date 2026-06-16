#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2022 Google LLC
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# Signals that the given finalized deal is ready to serve.
#
# By default, deals are set ready to serve as soon as they're finalized. For
# programmatic guaranteed deals, bidders can opt out of this feature by asking
# their account manager. This is recommended for programmatic guaranteed deals
# in order to ensure that bidders have creatives prepared to be used in placing
# bids once the deal is serving. Use finalizedDeals.addCreative to associate
# creatives with a programmatic guaranteed deal.

require 'optparse'

require_relative '../../../util'


def set_ready_to_serve_finalized_deals(marketplace, options)
  name = "buyers/#{options[:account_id]}/finalizedDeals/#{options[:deal_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::SetReadyToServeRequest.new()

  puts "Setting finalized deal with name '#{name}' as ready to serve:"

  finalized_deal = marketplace.set_finalized_deal_ready_to_serve(name, body)
  FinalizedDealPrinter.print(finalized_deal)
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise 'Unable to create service, was the KEY_FILE in util.rb set? Error message: #{e.message}'
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which the finalized deal was created. This will be used to '\
      'construct the name used as a path parameter for the finalizedDeals.setReadyToServe request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'deal_id',
      'The resource ID of the buyers.finalizedDeals resource that is being set ready to serve. This will be used to '\
      'construct the name used as a path parameter for the finalizedDeals.setReadyToServe request.',
      short_alias: 'd', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    set_ready_to_serve_finalized_deals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
