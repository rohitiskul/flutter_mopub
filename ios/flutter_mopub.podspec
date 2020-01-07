#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'flutter_mopub'
  s.version          = '0.0.1'
  s.summary          = 'A new flutter plugin project.'
  s.description      = <<-DESC
A new flutter plugin project.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.dependency 'mopub-ios-sdk','~> 5.9'
  # Google (AdMob & Ad Manager)
  s.dependency 'Google-Mobile-Ads-SDK'
  s.dependency 'MoPub-AdMob-Adapters', '7.52.0.3'

  # AppLovin
  s.dependency 'AppLovinSDK'
  s.dependency 'MoPub-Applovin-Adapters', '6.10.2.0'

  # Tapjoy
  s.dependency 'TapjoySDK', '~> 12.4'
  s.dependency 'MoPub-TapJoy-Adapters', '12.4.0.0'

  # Facebook Audience Network
  s.dependency 'FBAudienceNetwork'
  s.dependency 'MoPub-FacebookAudienceNetwork-Adapters', '5.6.0.0'

  # Unity Ads
  s.dependency 'UnityAds'
  s.dependency 'MoPub-UnityAds-Adapters', '3.4.0.0'

  # Vungle
  s.dependency 'VungleSDK-iOS', '~> 6.4'
  s.dependency 'MoPub-Vungle-Adapters', '6.4.6.1'

  # ironSource
  s.dependency 'IronSourceSDK'
  s.dependency 'MoPub-IronSource-Adapters', '6.11.0.0.0'

  s.ios.deployment_target = '9.0'
  s.static_framework = true
end

