/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 function closeDialogIfSucess(xhr, status, args, dialogWidget, dialogId) {
 if (args.validationFailed || args.KEEP_DIALOG_OPENED) {
 jQuery('#'+dialogId).effect("bounce", {times : 4, distance : 20}, 100);
 } 
 else {
 dialogWidget.hide();
 }
 }
 */
function closeDialogIfSucess(xhr, status, args, prepDialog, idPrepDialog) {
	if (args.validationFailed || args.KEEP_DIALOG_OPENED) {
		jQuery('#' + idPrepDialog).effect("bounce", {
			times : 4,
			distance : 20
		}, 100);
	} else {
		prepDialog.hide();
	}
}