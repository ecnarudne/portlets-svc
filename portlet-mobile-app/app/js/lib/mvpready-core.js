/* ========================================================
*
* MVP Ready - Lightweight & Responsive Admin Template
*
* ========================================================
*
* File: mvpready-core.js
* Theme Version: 1.1.0
* Bootstrap Version: 3.1.1
* Author: Jumpstart Themes
* Website: http://mvpready.com
*
* ======================================================== */

var mvpready_core = function () {

  "use strict"

  var getLayoutColors = function () {
    var colors
    colors = ['#f76b4c', '#d9144f', '#8b5cf5', '#31a0e5', '#0d67b5', '#041E35', '#773425'] // Mpt
    

    return colors
  }   

  var isLayoutCollapsed = function () {
    return $('.navbar-toggle').css ('display') == 'block'
  }

  var initFormValidation = function( ) {
    if ($.fn.parsley) {
      $('.parsley-form').each (function () {
        $(this).parsley ({
          trigger: 'change',
          errors: {
            container: function (element, isRadioOrCheckbox) {
              if (element.parents ('form').is ('.form-horizontal')) {
                return element.parents ("*[class^='col-']")
              }             
              return element.parents ('.form-group')
            }
          }
        })
      })
    }
  }

  var initAccordions = function () {
    $('.accordion-simple, .accordion-panel').each (function (i) {
      var accordion = $(this),
          toggle = accordion.find ('.accordion-toggle'),
          activePanel = accordion.find ('.panel-collapse.in').parent ()

      activePanel.addClass ('is-open')

      if (accordion.is ('.accordion-simple')) {
        toggle.prepend('<i class="fa accordion-caret"></i>')
      }

      toggle.on ('click', function (e) {
        var panel = $(this).parents ('.panel')

        panel.toggleClass ('is-open')
        panel.siblings ().removeClass ('is-open')
      })
    })
  }

  var initTooltips = function () {
    $('.ui-tooltip').tooltip ({ container: 'body' })
    $('.ui-popover').popover ({ container: 'body' })
  }

  var initBackToTop = function () {
    var backToTop = $('<a>', { id: 'back-to-top', href: '#top' }),
        icon = $('<i>', { 'class': 'fa fa-chevron-up' })

    backToTop.appendTo ('body')
    icon.appendTo (backToTop)

    backToTop.hide ()

    $(window).scroll (function () {
      if ($(this).scrollTop () > 150) {
        backToTop.fadeIn ()
      } else {
        backToTop.fadeOut ()
      }
    })

    backToTop.click (function (e) {
      e.preventDefault ()

      $('body, html').animate({
        scrollTop: 0
      }, 600)
    })
  }

  var navEnhancedInit = function () {
    $('.mainnav-menu').find ('> .active').addClass ('is-open')

    $('.mainnav-menu > .dropdown').on ('show.bs.dropdown', function () {
      $(this).addClass ('is-open')
      $(this).siblings ().removeClass ('is-open')
    })
  }

  var navHoverInit = function (config) {
    $('[data-hover="dropdown"]').each (function () {
      var $this = $(this),
          defaults = { delay: { show: 1000, hide: 1000 } },
          $parent = $this.parent (),
          settings = $.extend (defaults, config),
          timeout

      if (!('ontouchstart' in document.documentElement)) {
        $parent.find ('.dropdown-toggle').click (function (e) {
            if (!isLayoutCollapsed ()) {
              e.preventDefault ()
              e.stopPropagation ()
            }
        })
      }

      $parent.mouseenter(function () {
        if (isLayoutCollapsed ()) { return false }

        timeout = setTimeout (function () {
          $parent.addClass ('open')
          $parent.trigger ('show.bs.dropdown')
        }, settings.delay.show)
      })

      $parent.mouseleave(function () {
        if (isLayoutCollapsed ()) { return false }

        clearTimeout (timeout)

        timeout = setTimeout (function () {
          $parent.removeClass ('open keep-open')
          $parent.trigger ('hide.bs.dropdown')
        }, settings.delay.hide)
      })
    })
  }

  var initLightbox = function () {
    if ($.fn.magnificPopup) {
      $('.ui-lightbox').magnificPopup ({
        type: 'image',
        closeOnContentClick: false,
        closeBtnInside: true,
        fixedContentPos: true,
        mainClass: 'mfp-no-margins mfp-with-zoom',
        image: {
          verticalFit: true,
          tError: '<a href="%url%">The image #%curr%</a> could not be loaded.'
        }
      })

      $('.ui-lightbox-video, .ui-lightbox-iframe').magnificPopup ({
        disableOn: 700,
        type: 'iframe',
        mainClass: 'mfp-fade',
        removalDelay: 160,
        preloader: false,
        fixedContentPos: false
      })

      $('.ui-lightbox-gallery').magnificPopup ({
        delegate: 'a',
        type: 'image',
        tLoading: 'Loading image #%curr%...',
        mainClass: 'mfp-img-mobile',
        gallery: {
          enabled: true,
          navigateByImgClick: true,
          preload: [0,1]
        },
        image: {
          tError: '<a href="%url%">The image #%curr%</a> could not be loaded.',
          titleSrc: function(item) {
            return item.el.attr('title') + '<small>by Marsel Van Oosten</small>'
          }
        }
      })
    }
  }

  return {    
    navEnhancedInit: navEnhancedInit,
    navHoverInit: navHoverInit,

    initAccordions: initAccordions,   
    initFormValidation: initFormValidation,
    initTooltips: initTooltips,
    initBackToTop: initBackToTop,    
    initLightbox: initLightbox,
    isLayoutCollapsed: isLayoutCollapsed,

    layoutColors: getLayoutColors ()
  }

}()