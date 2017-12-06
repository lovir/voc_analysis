/*
 * SimpleModal Basic Modal Dialog
 * http://www.ericmmartin.com/projects/simplemodal/
 * http://code.google.com/p/simplemodal/
 *
 * Copyright (c) 2010 Eric Martin - http://ericmmartin.com
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Revision: $Id: basic.js 254 2010-07-23 05:14:44Z emartin24 $
 */

jQuery(function ($) {
	// Load dialog on page load
	//$('#basic-modal-content').modal();


	$(".modal_btn").click(function(idx){ 
		$('#basic-modal-'+$(this).attr("name")).modal({
			persist: false,  //이전 변경내용 유지 안함
			focus: false,	//포커스제거
			onClose: function () {
			   $('body').css('overflow','auto');
			   $.modal.close();
			}
		});

		$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
		return false;

	});

});
 

