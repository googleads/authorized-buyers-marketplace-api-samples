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
# Resumes serving of a given paused finalized deal.
#
# Note that only programmatic guaranteed deals can be resumed by buyers at this
# time.

require 'optparse'

require_relative '../../../util'


def resume_finalized_deals(marketplace, options)
  name = "buyers/#{options[:account_id]}/finalizedDeals/#{options[:deal_id]}"

  body = Google::Apis::AuthorizedbuyersmarketplaceV1::ResumeFinalizedDealRequest.new()

  puts "Resuming finalized deal with name '#{name}':"

  finalized_deal = marketplace.resume_finalized_deal(name, body)
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
      'construct the name used as a path parameter for the finalizedDeals.resume request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'deal_id',
      'The resource ID of the buyers.finalizedDeals resource that is being resumed. This will be used to construct '\
      'the name used as a path parameter for the finalizedDeals.resume request.',
      short_alias: 'd', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    resume_finalized_deals(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
